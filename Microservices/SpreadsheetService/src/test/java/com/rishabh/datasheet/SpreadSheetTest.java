package com.rishabh.datasheet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.stockmarket.datasheet.config.WebConfig;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class SpreadSheetTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void test() throws Exception {

        MultipartFile fileStock = new MockMultipartFile("filename", "stock-market.xlxs", "application/vnd.ms-excel", new ClassPathResource("stock-market.xlxs").getInputStream());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform((RequestBuilder) ((ResultActions) MockMvcRequestBuilders.multipart("/upload")
        		.file((MockMultipartFile) fileStock))
                .andExpect(status().is(200)));
    }
}