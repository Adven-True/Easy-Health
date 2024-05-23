package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

	//data or not
	Hospital getHospitalByHoscode(String hoscode);

	//Fuzzy query based on hospital name
	List<Hospital> findHospitalByHosnameLike(String hosname);
}
