package jon.marketdata.stocker.filedatasource;

import jon.marketdata.stocker.config.FileDataSourceConfig;
import jon.marketdata.stocker.interfaces.FileDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class IntradayFileData extends FileDataSource {
    private FileDataSourceConfig fileConfig;

    public IntradayFileData(FileDataSourceConfig fileConfig){
        this.fileConfig = fileConfig;
    }

    @Override
    protected void setResource() {
        this.resource = new ClassPathResource(fileConfig.getIntraday());
    }
}
