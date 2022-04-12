package kopo.poly.service.impl;

import kopo.poly.dto.OcrDTO;
import kopo.poly.persistance.mapper.IImgMapper;
import kopo.poly.service.IImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service("ImgService")
public class ImgService implements IImgService {

    @Resource(name="IImgMapper")
    private IImgMapper imgMapper;

//파일 저장 경로
    @Override
    public int insertFilePath(OcrDTO fDTO) throws Exception {

        log.info(this.getClass().getName()+". insetfilePath Start!");

        int res =imgMapper.insertFilePath(fDTO);

        log.info(this.getClass().getName()+". insetfilePath end!");

        return res;
    }

    @Override
    public OcrDTO getPath(OcrDTO fDTO) {
        log.info(this.getClass().getName()+"getsavePath Start!");

        fDTO = imgMapper.getFilePath(fDTO);

        log.info(this.getClass().getName()+"getsavePath end!");
        return fDTO;
    }
}
