package pl.bernic.ion.spring.graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.bernic.ion.spring.graphql.domain.Employee;
import pl.bernic.ion.spring.graphql.filter.EmployeeFilter;
import pl.bernic.ion.spring.graphql.filter.FilterField;
import pl.bernic.ion.spring.graphql.repository.EmployeeRepository;

@Component
public class EmployeeQueryResolver implements GraphQLQueryResolver {

    EmployeeRepository employeeRepository;

    EmployeeQueryResolver(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Iterable<Employee> employees() {
        return employeeRepository.findAll();
    }

    public Employee employee(Integer id) {
        return employeeRepository.findById(id).get();
    }

    public Iterable<Employee> employeesWithFilter(EmployeeFilter filter) {
        Specification<Employee> spec = null;
        if (filter.getSalary() != null)
            spec = bySalary(filter.getSalary());
        if (filter.getAge() != null)
            spec = (spec == null ? byAge(filter.getAge()) : spec.and(byAge(filter.getAge())));
        if (filter.getPosition() != null)
            spec = (spec == null ? byPosition(filter.getPosition()) :
                    spec.and(byPosition(filter.getPosition())));
        if (spec != null)
            return employeeRepository.findAll(spec);
        else
            return employeeRepository.findAll();
    }

    private Specification<Employee> bySalary(FilterField filterField) {
        return (Specification<Employee>) (root, query, builder) -> filterField.generateCriteria(builder, root.get("salary"));
    }

    private Specification<Employee> byAge(FilterField filterField) {
        return (Specification<Employee>) (root, query, builder) -> filterField.generateCriteria(builder, root.get("age"));
    }

    private Specification<Employee> byPosition(FilterField filterField) {
        return (Specification<Employee>) (root, query, builder) -> filterField.generateCriteria(builder, root.get("position"));
    }
}
