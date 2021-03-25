package com.a202.fishserver.service.collection;

import com.a202.fishserver.domain.collection.Collection;
import com.a202.fishserver.domain.collection.CollectionRepository;
import com.a202.fishserver.domain.fish.Fish;
import com.a202.fishserver.domain.fish.FishRepository;
import com.a202.fishserver.domain.fishImage.FishImage;
import com.a202.fishserver.domain.fishImage.FishImageRepository;
import com.a202.fishserver.domain.user.User;
import com.a202.fishserver.domain.user.UserRepository;
import com.a202.fishserver.dto.collection.CollectionGetDetailResponseDto;
import com.a202.fishserver.dto.collection.CollectionGetResponseDto;
import com.a202.fishserver.dto.collection.CollectionPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl {
    private final CollectionRepository collectionRepository;
    private final FishImageRepository fishImageRepository;
    private final FishRepository fishRepository;
    private final UserRepository userRepository;

    /**
     * 내 보관함 조회
     */
    public List<CollectionGetResponseDto> getMyCollections(long userId){
        List<Collection> list = collectionRepository.findByUser(new User(userId));
        List<CollectionGetResponseDto> result = new ArrayList<>();

        for (Collection c : list) {
            Optional<FishImage> fishImage = fishImageRepository.findByCollection(c);
            String imagePath = "";
            if (fishImage.isPresent()) imagePath = fishImage.get().getImagePath();

            result.add(CollectionGetResponseDto.builder()
                    .collectionId(c.getId())
                    .fishImage(imagePath)
                    .build());
        }
        return result;
    }

    /**
     * 도감 상세 조회
     */
    public CollectionGetDetailResponseDto getCollectionDetail (int collectionId) throws Exception{
        Optional<Collection> collection = collectionRepository.findById(collectionId);
        if (!collection.isPresent()) throw new Exception("도감이 존재하지 않습니다.");

        Optional<FishImage> fishImage = fishImageRepository.findByCollection(collection.get());
        String imagePath = "";
        if (fishImage.isPresent()) imagePath = fishImage.get().getImagePath();

        return CollectionGetDetailResponseDto.builder()
                .collectionId(collectionId)
                .fishName(collection.get().getFish().getName())
                .fishImage(imagePath)
                .fishLength(collection.get().getLength())
                .fishLocation(collection.get().getLocation())
                .fishMemo(collection.get().getMemo())
                .fishBait(collection.get().getBait())
                .fishingInfo(collection.get().getFishingInfo())
                .regDate(collection.get().getRegDate())
                .build();
    }

    /**
     * 물고기 등록
     */
    public void postCollection(CollectionPostRequestDto dto) throws Exception{
        Optional<User> user = userRepository.findById(dto.getUser_id());
        Optional<Fish> fish = fishRepository.findById(dto.getFish_id());
        if (!user.isPresent()) throw new Exception("해당 사용자가 존재하지 않습니다.");
        if (!fish.isPresent()) throw new Exception("해당 물고기가 존재하지 않습니다.");

        collectionRepository.save(Collection.builder()
                                    .fish(fish.get())
                                    .bait(dto.getBait())
                                    .fishingInfo(dto.getFishing_info())
                                    .length(dto.getLength())
                                    .location(dto.getLocation())
                                    .memo(dto.getMemo())
                                    .user(user.get())
                                    .regDate(LocalDateTime.now())
                                    .flag(false)
                                    .build());
    }

    /**
     * 도감 정보 수정
     */
    public void putCollection(CollectionPostRequestDto dto, int collectionId) throws Exception{
        Optional<Collection> collection = collectionRepository.findById(collectionId);
        Optional<User> user = userRepository.findById(dto.getUser_id());
        Optional<Fish> fish = fishRepository.findById(dto.getFish_id());
        if (!collection.isPresent()) throw new Exception("해당 도감이 존재하지 않습니다.");
        if (!user.isPresent()) throw new Exception("해당 사용자가 존재하지 않습니다.");
        if (!fish.isPresent()) throw new Exception("해당 물고기가 존재하지 않습니다.");

        collectionRepository.save(Collection.builder()
                .id(collectionId)
                .fish(fish.get())
                .bait(dto.getBait())
                .fishingInfo(dto.getFishing_info())
                .length(dto.getLength())
                .location(dto.getLocation())
                .memo(dto.getMemo())
                .user(user.get())
                .regDate(LocalDateTime.now())
                .flag(false)
                .build());
    }
}