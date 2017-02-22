package server.adapters;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class MvcAdapter extends WebMvcConfigurerAdapter {

	private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public ViewInterceptorAdapter viewInterceptor() {
        return new ViewInterceptorAdapter();
    }

    public @Override void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(viewInterceptor());
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	File theDir = new File("modules/");

		if (!theDir.exists()) {
			try{
				theDir.mkdir();
			} 
			catch(SecurityException e){
				e.printStackTrace();
			}
		}
    	
      String myExternalFilePath = "file:"+theDir.getAbsolutePath()+File.separator;
      
      log.info("\n" + myExternalFilePath + "\n");
      registry.addResourceHandler("/modules/**").addResourceLocations(myExternalFilePath);

      super.addResourceHandlers(registry);
    }
}
