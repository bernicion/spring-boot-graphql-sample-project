package pl.bernic.ion.spring.graphql.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.bernic.ion.spring.graphql.domain.Employee;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeContext {
    private List<Employee> employees;
}
