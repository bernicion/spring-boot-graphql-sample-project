package pl.bernic.ion.spring.graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@Component
public class OrganizationQueryResolver implements GraphQLQueryResolver {

    OrganizationRepository repository;

    OrganizationQueryResolver(OrganizationRepository repository) {
        this.repository = repository;
    }

    public Iterable<Organization> organizations() {
        return repository.findAll();
    }

    public Organization organization(Integer id) {
        return repository.findById(id).orElseThrow();
    }
}
