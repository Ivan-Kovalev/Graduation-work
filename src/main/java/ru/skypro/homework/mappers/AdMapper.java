package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "author", expression = "java(adEntity.getAuthor().getId())")
    Ad mapAdEntityToAd(AdEntity adEntity);

    @Mapping(source = "author", target = "author", qualifiedByName = "mapIntegerToUserEntity")
    AdEntity mapAdToAdEntity(Ad ad);

    CreateOrUpdateAd mapAdEnittyToCreateOrUpdateAd(AdEntity adEntity);

    AdEntity mapCreateOrUpdateAdToEntity(CreateOrUpdateAd createOrUpdateAd);

    ExtendedAd mapAdEntityToExtendedAd(AdEntity adEntity);

    AdEntity mapExtendedAdToAdEntity(ExtendedAd extendedAd);

    @Named("mapIntegerToUserEntity")
    default UserEntity map(Integer value) {
        if (value == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(value);
        return userEntity;
    }

}
