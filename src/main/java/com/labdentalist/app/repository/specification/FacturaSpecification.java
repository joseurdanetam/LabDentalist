package com.labdentalist.app.repository.specification;

import com.labdentalist.app.domain.Cliente;
import com.labdentalist.app.domain.Factura;
import com.labdentalist.app.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FacturaSpecification extends JpaSpecificationExecutor<Factura> {
    public static Specification<Factura> busquedaFacturas(String filtro) {
        return new Specification<Factura>() {
            private static final long serialVersionUID = 1L;

            public Predicate toPredicate(Root<Factura> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);

                List<Predicate> ors = new ArrayList<Predicate>();

                Join<Factura, Cliente> cliente = root.join("cliente", JoinType.INNER);

                Expression<String> numeroFactura = root.get("numeroFactura").as(String.class);

                String[] parametroBusqueda = filtro.split(" ");

                for (int i = 0; i < parametroBusqueda.length; i++) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    predicates.add(builder.like(cliente.<String>get("dni"), "%" + parametroBusqueda[i] + "%"));
                    predicates.add(builder.like(cliente.<String>get("nombre"), "%" + parametroBusqueda[i] + "%"));
                    predicates.add(builder.like(cliente.<String>get("apellido"), "%" + parametroBusqueda[i] + "%"));

                    predicates.add(builder.like(numeroFactura, "%" + parametroBusqueda[i] + "%"));

                    ors.add(builder.or(predicates.toArray(new Predicate[] {})));
                }
                Predicate result = builder.and(ors.toArray(new Predicate[] {}));
                return result;
            }
        };
    }
}
