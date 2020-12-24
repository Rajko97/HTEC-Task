package com.rajko.stefanexpress.repository.mapper

interface Mapper<I, O> {
    fun map(input: I) : O
}