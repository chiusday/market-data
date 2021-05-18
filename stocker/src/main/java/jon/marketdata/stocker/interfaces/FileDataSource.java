package jon.marketdata.stocker.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class FileDataSource implements DataSource {
    protected Resource resource;

    @PostConstruct
    protected abstract void setResource();

    @Override
    public String getStringData() {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
