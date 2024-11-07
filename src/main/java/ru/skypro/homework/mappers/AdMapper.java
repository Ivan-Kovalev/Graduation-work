package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;

@Mapper
public interface AdMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    Ad mapAdEntityToAd(AdEntity adEntity);

    AdEntity mapAdToAdEntity(Ad ad);

    CreateOrUpdateAd mapAdEnittyToCreateOrUpdateAd(AdEntity adEntity);

    AdEntity mapCreateOrUpdateAdToEntity(CreateOrUpdateAd createOrUpdateAd);

    ExtendedAd mapAdEntityToExtendedAd(AdEntity adEntity);

    AdEntity mapExtendedAdToAdEntity(ExtendedAd extendedAd);

}
