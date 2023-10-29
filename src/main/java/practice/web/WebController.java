package practice.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import practice.okhttp.okhttp.TestModel;

@RestController
@RequiredArgsConstructor
public class WebController {

    private final WebService service;

    @GetMapping("/myBoot")
    public List<TestModel> myBoot() {
        return service.myBoot();
    }

    @GetMapping("/myBoot2")
    public List<TestModel> myBoot2() {
        return service.myBoot2();
    }
}
