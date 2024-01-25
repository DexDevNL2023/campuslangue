package net.ktccenter.campusApi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface GenericMapper<E, D, R, L, I> {
    E asEntity(D dto);
    //Get one entity
    R asDTO(E entity);
    L asLite(E entity);
    //Importation
    List<E> asEntityList(List<I> dtoList);
    //Listings
    List<L> asDTOList(List<E> entityList);
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget E entity, D dto);
}
