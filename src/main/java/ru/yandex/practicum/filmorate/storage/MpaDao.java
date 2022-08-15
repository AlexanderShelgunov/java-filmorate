package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public interface MpaDao {

    Mpa getMpa(int mpaId);

    List<Mpa> getAllMpa();

}
