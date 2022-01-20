package pl.bernic.ion.spring.graphql.resolver;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;
import pl.bernic.ion.spring.graphql.domain.Department;
import pl.bernic.ion.spring.graphql.domain.Employee;
import pl.bernic.ion.spring.graphql.domain.EmployeeInput;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.DepartmentRepository;
import pl.bernic.ion.spring.graphql.repository.EmployeeRepository;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@Component
public class EmployeeMutableResolver implements GraphQLMutationResolver {

    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;
    OrganizationRepository organizationRepository;

    EmployeeMutableResolver(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    public Employee newEmployee(EmployeeInput employeeInput) {
        Department department = departmentRepository.findById(employeeInput.getDepartmentId()).get();
        Organization organization = organizationRepository.findById(employeeInput.getOrganizationId()).get();
        return employeeRepository.save(new Employee(null, employeeInput.getFirstName(), employeeInput.getLastName(),
                employeeInput.getPosition(), employeeInput.getAge(), employeeInput.getSalary(),
                department, organization));
    }
}
