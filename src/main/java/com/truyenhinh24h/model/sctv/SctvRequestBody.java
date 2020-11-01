package com.truyenhinh24h.model.sctv;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SctvRequestBody {
	
	private String maKenh;
	
	private String ngay;

}
