package ru.spb.tksoft.banking.controller;

/**
 * Custom user from JWT.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public record JwtUser(long userId, String email) {}
