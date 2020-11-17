package com.truyenhinh24h.utils;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public <F, T> T fromTo(F input, Class<T> destinationClass) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(input, destinationClass);
    }

    public <F, T> List<T> fromToList(List<F> input, Class<T> destinationClass) {
        return input.stream().map(item -> fromTo(item, destinationClass)).collect(Collectors.toList());
    }

}
