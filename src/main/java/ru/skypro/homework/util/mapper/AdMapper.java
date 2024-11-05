package ru.skypro.homework.util.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

@Component
public class AdMapper {

    public Ad mapAdEntityToAd(AdEntity adEntity) {
        return new Ad(
                adEntity.getAuthor().getId(),
                adEntity.getImage(),
                adEntity.getPrice(),
                adEntity.getPk(),
                adEntity.getTitle()
        );
    }

    public AdEntity mapAdToAdEntity(Ad ad) {
        return new AdEntity(
                ad.getPk(),
                ad.getImage(),
                ad.getTitle(),
                ad.getPrice(),
                new UserEntity(ad.getPk())
        );
    }

    public CreateOrUpdateAd mapAdEnittyToCreateOrUpdateAd(AdEntity adEntity) {
        return new CreateOrUpdateAd(
                adEntity.getTitle(),
                adEntity.getPrice(),
                adEntity.getDescription()
        );
    }

    public AdEntity mapCreateOrUpdateAdToEntity(CreateOrUpdateAd createOrUpdateAd) {
        return new AdEntity(
                createOrUpdateAd.getTitle(),
                createOrUpdateAd.getPrice(),
                createOrUpdateAd.getDescription()
        );
    }

    public ExtendedAd mapAdEntityToExtendedAd(AdEntity adEntity) {
        return new ExtendedAd(
                adEntity.getPk(),
                adEntity.getAuthor().getFirstName(),
                adEntity.getAuthor().getLastName(),
                adEntity.getDescription(),
                adEntity.getAuthor().getEmail(),
                adEntity.getImage(),
                adEntity.getAuthor().getPhone(),
                adEntity.getPrice(),
                adEntity.getTitle()
        );
    }

    public AdEntity mapExtendedAdToAdEntity(ExtendedAd extendedAd) {
        UserEntity author = new UserEntity(
                extendedAd.getAuthorFirstName(),
                extendedAd.getAuthorLastName(),
                extendedAd.getEmail(),
                extendedAd.getPhone()
        );
        return new AdEntity(
                extendedAd.getPk(),
                extendedAd.getImage(),
                extendedAd.getPrice(),
                extendedAd.getTitle(),
                extendedAd.getDescription(),
                author
        );
    }

}
