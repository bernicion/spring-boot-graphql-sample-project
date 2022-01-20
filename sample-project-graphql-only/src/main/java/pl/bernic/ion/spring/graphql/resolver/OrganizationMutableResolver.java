package pl.bernic.ion.spring.graphql.resolver;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.domain.OrganizationInput;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@Component
public class OrganizationMutableResolver implements GraphQLMutationResolver {

    OrganizationRepository organizationRepository;

    OrganizationMutableResolver(OrganizationRepository organizationRepository){
        this.organizationRepository = organizationRepository;
    }

    public Organization newOrganization(OrganizationInput organizationInput) {
        return organizationRepository.save(new Organization(null, organizationInput.getName(), null, null));
    }
}
