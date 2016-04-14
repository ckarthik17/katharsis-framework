package io.katharsis.resource.registry.repository;

import io.katharsis.repository.ParametersFactory;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.repository.RepositoryInstanceBuilder;
import io.katharsis.repository.RepositoryMethodParameterProvider;
import io.katharsis.repository.adapter.RelationshipRepositoryAdapter;
import io.katharsis.repository.annotations.JsonApiRelationshipRepository;
import io.katharsis.utils.ClassUtils;
import io.katharsis.utils.java.Optional;

public class AnnotatedRelationshipEntryBuilder<T, D> implements RelationshipEntry<T, D> {

    private RepositoryInstanceBuilder repositoryInstanceBuilder;

    public AnnotatedRelationshipEntryBuilder(RepositoryInstanceBuilder repositoryInstanceBuilder) {
        this.repositoryInstanceBuilder = repositoryInstanceBuilder;
    }

    @Override
    public Class<?> getTargetAffiliation() {
        @SuppressWarnings("unchecked")
        final Optional<JsonApiRelationshipRepository> annotation = ClassUtils.getAnnotation(
                repositoryInstanceBuilder.getRepositoryClass(),
                JsonApiRelationshipRepository.class
        );

        if (annotation.isPresent()) {
            return annotation.get().target();
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            "Class %s must be annotated with @JsonApiRelationshipRepository",
                            repositoryInstanceBuilder.getClass().getName()
                    )
            );
        }
    }

    public RelationshipRepository<T, ?, ?, ?> build(RepositoryMethodParameterProvider parameterProvider) {
        return new RelationshipRepositoryAdapter<>(repositoryInstanceBuilder.buildRepository(), new ParametersFactory(parameterProvider));
    }

    @Override
    public String toString() {
        return "AnnotatedRelationshipEntryBuilder{" +
                "repositoryInstanceBuilder=" + repositoryInstanceBuilder +
                '}';
    }
}
