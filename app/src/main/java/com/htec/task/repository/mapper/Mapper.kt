package com.htec.task.repository.mapper

interface Mapper<I, O> {
    fun map(input: I) : O
}