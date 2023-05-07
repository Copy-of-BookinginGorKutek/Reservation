package com.b2.reservation.service;

import com.b2.reservation.exceptions.KuponDoesNotExistException;
import com.b2.reservation.model.kupon.Kupon;
import com.b2.reservation.repository.KuponRepository;
import com.b2.reservation.request.KuponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KuponServiceImpl implements KuponService {
    private final KuponRepository kuponRepository;

    @Override
    public Kupon create(KuponRequest kuponRequest) {
        Kupon kupon = Kupon.builder()
                .name(kuponRequest.getName())
                .percentageDiscounted(kuponRequest.getPercentageDiscounted())
                .build();
        kupon = kuponRepository.save(kupon);
        return kupon;
    }

    @Override
    public List<Kupon> findAll() {
        return kuponRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        if (kuponRepository.findById(id).isEmpty()){
            throw new KuponDoesNotExistException(id);
        }
        kuponRepository.deleteById(id);
    }
}
