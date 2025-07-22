package org.scoula.chapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.chapi.domain.CHOfficetelModelVO;
import org.scoula.chapi.domain.CHOfficetelVO;
import org.scoula.chapi.dto.CHOfficetelDTO;
import org.scoula.chapi.dto.CHOfficetelModelDTO;
import org.scoula.chapi.dto.OfficetelModelResponse;
import org.scoula.chapi.dto.OfficetelResponse;
import org.scoula.chapi.mapper.DBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@PropertySource("classpath:application.properties")
@Log4j2
@Service
@RequiredArgsConstructor
public class OfficetelDBServiceImpl implements OfficetelDBService {
    final DBMapper mapper;

    final RestTemplate restTemplate;

    @Value("${applyHome.api_key}")
    private String applyHomeApiKey;


    // ApplyHome Officetel Data
    @Transactional
    @Override
    public int insert(CHOfficetelDTO dto) {
        CHOfficetelVO vo = dto.toVO();

        return mapper.insert(vo);
    }


    @Transactional
    @Override
    public int insertAll(List<CHOfficetelDTO> dtoList) {
        int affectedRows = 0;
        for(CHOfficetelDTO dto : dtoList){
            try {
                CHOfficetelVO vo = dto.toVO();
                int result = mapper.insert(vo);
                if(result > 0){
                    affectedRows++;
                } else {
                    log.info("PBLANC_NO:  "+dto.getPblancNo()+" Data updated but No records altered");
                }
            } catch (Exception e) {
                log.info("PBLANC_NO: "+dto.getPblancNo()+" Error occured: "+e.getMessage());
                throw new RuntimeException("Error occured: ", e);
            }
        }
        log.info("Total inserted rows: "+dtoList.size()+" Affected Rows: "+affectedRows);
        return affectedRows;
    }

    @Transactional
    @Override
    public int fetchAndInsertAPIData() {
        String baseUrl = "https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getUrbtyOfctlLttotPblancDetail";
        log.info("Fetch data from ApplyHome API: {}", baseUrl);

        String apiUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", applyHomeApiKey)
                .queryParam("page", 1)
                .queryParam("perPage", 200)
                .build(false).toUriString();

        List<CHOfficetelDTO> fetchedDtoList;

        try{
            log.info("Fetch data from ApplyHome API: {}", apiUrl);
            OfficetelResponse apiOfficetelResponse = restTemplate.getForObject(apiUrl, OfficetelResponse.class);
            if(apiOfficetelResponse !=null && apiOfficetelResponse.getData()!=null){
                fetchedDtoList = apiOfficetelResponse.getData();
                log.info("Fetched data from ApplyHome API: {}", fetchedDtoList.size());
            } else {
                log.info("Fetched data from ApplyHome API: null");
                return 0;
            }
            int affectedRows = insertAll(fetchedDtoList);
            log.info("Total inserted rows: "+affectedRows);
            return affectedRows;

        } catch (Exception e) {
            log.error("Error occured: ", e.getMessage(), e);
            throw new RuntimeException("Error occured: ", e);
        }
    }

    // ApplyHome Officetel Model Data
    @Transactional
    @Override
    public int insertOfficetelModel(CHOfficetelModelDTO dto) {
        CHOfficetelModelVO vo = dto.toVO();

        return mapper.insertOfficetelModel(vo);
    }

    @Transactional
    @Override
    public int insertAllOfficetelModel(List<CHOfficetelModelDTO> dtoList) {
        int affectedRows = 0;
        for(CHOfficetelModelDTO dto : dtoList){
            try {
                CHOfficetelModelVO vo = dto.toVO();
                int result = mapper.insertOfficetelModel(vo);
                if(result > 0){
                    affectedRows++;
                } else {
                    log.info("PBLANC_NO:  "+dto.getPblancNo()+" MODEL_NO: "+dto.getModelNo()+" Data updated but No records altered");
                }
            } catch (Exception e) {
                log.info("PBLANC_NO:  "+dto.getPblancNo()+" MODEL_NO: "+dto.getModelNo()+" Error occured: "+e.getMessage());
                throw new RuntimeException("Error occured: ", e);
            }
        }
        log.info("Total inserted rows: "+dtoList.size()+" Affected Rows: "+affectedRows);
        return affectedRows;
    }

    @Transactional
    @Override
    public int fetchOfficetelModel() {
        String baseUrl = "https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getUrbtyOfctlLttotPblancMdl";
        log.info("Fetch Model data from ApplyHome API: {}", baseUrl);

        String apiUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", applyHomeApiKey)
                .queryParam("page", 1)
                .queryParam("perPage", 20)
                .build(false).toUriString();

        List<CHOfficetelModelDTO> fetchedDtoList;

        try{
            log.info("Fetch Model data from ApplyHome API: {}", apiUrl);
            OfficetelModelResponse apiOfficetelModelResponse = restTemplate.getForObject(apiUrl, OfficetelModelResponse.class);
            if(apiOfficetelModelResponse !=null && apiOfficetelModelResponse.getData()!=null){
                fetchedDtoList = apiOfficetelModelResponse.getData();
                log.info("Fetched Model data from ApplyHome API: {}", fetchedDtoList.size());
            } else {
                log.info("Fetched Model data from ApplyHome API: null");
                return 0;
            }
            int affectedRows = insertAllOfficetelModel(fetchedDtoList);
            log.info("Total inserted rows: "+affectedRows);
            return affectedRows;

        } catch (Exception e) {
            log.error("Error occured: ", e.getMessage(), e);
            throw new RuntimeException("Error occured: ", e);
        }
    }


}
