package com.maaksoft.cassandra.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import org.springframework.data.cassandra.repository.Query;

import org.springframework.stereotype.Repository;

import com.maaksoft.cassandra.model.Data;

@Repository
public interface DataRepository extends CassandraRepository<Data, UUID> {

    @Query("select * from data where value = ?0 ")
    Iterable<Data> findByValue(String value);

}