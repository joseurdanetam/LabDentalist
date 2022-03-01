import {
  Component,
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef,
  OnInit,
  AfterViewInit,
  AfterViewChecked,
  ChangeDetectorRef,
} from '@angular/core';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarMonthViewComponent,
  CalendarView,
} from 'angular-calendar';
import { CitaService } from '../service/cita.service';
import { ICita } from '../cita.model';
import { HttpResponse } from '@angular/common/http';
import { ViewEncapsulation } from '@angular/compiler';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA',
  },
};

@Component({
  selector: 'jhi-calendar-cita-component',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './calendar-cita.component.html',
})
export class CalendarCitaComponent implements OnInit {
  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any> | undefined;

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  viewDate: Date = new Date();

  modalData:
    | {
        action: string;
        event: CalendarEvent;
      }
    | undefined;

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter(iEvent => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];

  refresh = new Subject<void>();

  allCitas: ICita[] = [];

  events: CalendarEvent[] = [];

  activeDayIsOpen = true;

  constructor(private modal: NgbModal, protected citaService: CitaService, protected cdr: ChangeDetectorRef) {}

  allCitasQuery(): void {
    //this.events.length=0;

    this.citaService.query().subscribe({
      next: (res: HttpResponse<ICita[]>) => {
        this.allCitas = res.body ?? [];
        this.allCitas.forEach(item => {
          const titulo = `${item.descripcion ?? ''}
          con DNI ${item.cliente?.dni ?? ''}
          y nombre ${item.cliente?.nombre ?? ''}  ${item.cliente?.apellido ?? ''}`;
          this.events.push({
            start: item.fechaCita != null ? new Date(item.fechaCita.format()) : new Date(),
            end: item.fechaCita != null ? new Date(item.fechaCita.format()) : new Date(),
            title: titulo,
            color: colors.yellow,
            actions: this.actions,
          });
        });
        this.viewDate = new Date();
        this.handleEvent('Última cita asignada', this.events[this.events.length - 1]);
      },
    });
  }

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if ((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) || events.length === 0) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  eventTimesChanged({ event, newStart, newEnd }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map(iEvent => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.red,
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];
  }

  deleteEvent(eventToDelete: CalendarEvent): void {
    this.events = this.events.filter(event => event !== eventToDelete);
  }

  setView(view: CalendarView): void {
    this.view = view;
  }

  closeOpenMonthViewDay(): void {
    this.activeDayIsOpen = false;
  }

  ngOnInit(): void {
    this.allCitasQuery();
  }
}
