package br.com.fabricio.ama.amadesafio.services;

import java.util.Set;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Service
public class FilterCamposService {

    public static MappingJacksonValue filtroCampos(Set<String> campos, String nomeFiltro, Object dataSet){
        SimpleBeanPropertyFilter filtro = SimpleBeanPropertyFilter.serializeAllExcept(campos);
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(nomeFiltro, filtro);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(dataSet);
        jacksonValue.setFilters(filterProvider);
        return jacksonValue;
    }
}
