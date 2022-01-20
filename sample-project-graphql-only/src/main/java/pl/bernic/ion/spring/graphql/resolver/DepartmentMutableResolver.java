package pl.bernic.ion.spring.graphql.resolver;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;
import pl.bernic.ion.spring.graphql.domain.Department;
import pl.bernic.ion.spring.graphql.domain.DepartmentInput;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.DepartmentRepository;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@Component
public class DepartmentMutableResolver implements GraphQLMutationResolver {
    DepartmentRepository departmentRepository;
    OrganizationRepository organizationRepository;

    DepartmentMutableResolver(DepartmentRepository departmentRepository, OrganizationRepository organizationRepository){
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
    }

    public Department newDepartment(DepartmentInput departmentInput){
        Organization organization = organizationRepository.findById(departmentInput.getOrganizationId()).get();
        return departmentRepository.save(new Department(null, departmentInput.getName(), null, organization));
    }
}
