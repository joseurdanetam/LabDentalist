package com.labdentalist.app.repository.specification;

import com.labdentalist.app.domain.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClienteSpecification extends JpaSpecificationExecutor<Cliente> {
    public static Specification<Cliente> busquedaCliente(String filter) {
        return new Specification<Cliente>() {
            private static final long serialVersionUID = 1L;

            public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);
                List<Predicate> ors = new ArrayList<Predicate>();

                Expression<String> dni = root.get("dni").as(String.class);
                Expression<String> nombre = root.get("nombre").as(String.class);
                Expression<String> apellido = root.get("apellido").as(String.class);

                String[] parametroBusqueda = filter.split(" ");
                for (int i = 0; i < parametroBusqueda.length; i++) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    predicates.add(builder.like(dni, "%" + parametroBusqueda[i] + "%"));
                    predicates.add(builder.like(nombre, "%" + parametroBusqueda[i] + "%"));
                    predicates.add(builder.like(apellido, "%" + parametroBusqueda[i] + "%"));

                    ors.add(builder.or(predicates.toArray(new Predicate[] {})));
                }
                Predicate result = builder.and(ors.toArray(new Predicate[] {}));
                return result;
            }
        };
    }
}
