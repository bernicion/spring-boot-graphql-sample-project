package pl.bernic.ion.spring.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.data.jpa.domain.Specification;
import pl.bernic.ion.spring.graphql.context.EmployeeContext;
import pl.bernic.ion.spring.graphql.domain.Department;
import pl.bernic.ion.spring.graphql.domain.Employee;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.DepartmentRepository;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Set;
import java.util.stream.Collectors;

@DgsComponent
public class DepartmentFetcher {
    private DepartmentRepository repository;

    DepartmentFetcher(DepartmentRepository repository) {
        this.repository = repository;
    }

    @DgsData(parentType = "QueryResolver", field = "departments")
    public Iterable<Department> findAll(DataFetchingEnvironment environment) {
        DataFetchingFieldSelectionSet s = environment.getSelectionSet(); // (1)
        if (s.contains("employees") && !s.contains("organization")) // (2)
            return repository.findAll(fetchEmployees());
        else if (!s.contains("employees") && s.contains("organization"))
            return repository.findAll(fetchOrganization());
        else if (s.contains("employees") && s.contains("organization"))
            return repository.findAll(fetchEmployees().and(fetchOrganization()));
        else
            return repository.findAll();
    }

    @DgsData(parentType = "QueryResolver", field = "department")
    public Department findById(@InputArgument("id") Integer id,
                               DataFetchingEnvironment environment) { // (3)
        Specification<Department> spec = byId(id);
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        EmployeeContext employeeContext = DgsContext.getCustomContext(environment); // (4)
        Set<Employee> employees = null;
        if (selectionSet.contains("employees")) {
            if (employeeContext.getEmployees().size() == 0) // (5)
                spec = spec.and(fetchEmployees());
            else
                employees = employeeContext.getEmployees().stream()
                        .filter(emp -> emp.getDepartment().getId().equals(id))
                        .collect(Collectors.toSet());
        }
        if (selectionSet.contains("organization"))
            spec = spec.and(fetchOrganization());
        Department department = repository
                .findOne(spec).orElseThrow(DgsEntityNotFoundException::new);
        if (employees != null)
            department.setEmployees(employees);
        return department;
    }

    private Specification<Department> fetchOrganization() {
        return (root, query, builder) -> {
            Fetch<Department, Organization> f = root.fetch("organization", JoinType.LEFT);
            Join<Department, Organization> join = (Join<Department, Organization>) f;
            return join.getOn();
        };
    }

    private Specification<Department> fetchEmployees() {
        return (root, query, builder) -> {
            Fetch<Department, Employee> f = root.fetch("employees", JoinType.LEFT);
            Join<Department, Employee> join = (Join<Department, Employee>) f;
            return join.getOn();
        };
    }

    private Specification<Department> byId(Integer id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
}
