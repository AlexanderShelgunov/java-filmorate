package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    @Autowired
    private MpaDao mpaDao;

    @GetMapping
    public List<Mpa> getAllMpa() {
        log.info("Текущее количество МПА: {}", mpaDao.getAllMpa().size());
        return mpaDao.getAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa findMpaById(@PathVariable int mpaId) {
        log.info("МПА {} полученый по ID={}", mpaDao.getMpa(mpaId), mpaId);
        return mpaDao.getMpa(mpaId);
    }

}
