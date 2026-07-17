package br.com.dwnl.marketplace.catalog.infrastructure.http;

import br.com.dwnl.marketplace.catalog.application.BrowseShowcaseUseCase;
import br.com.dwnl.marketplace.catalog.application.dto.EventOutput;
import br.com.dwnl.marketplace.catalog.domain.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/showcase")
public class ShowCaseController {
    private final BrowseShowcaseUseCase browseShowcaseUseCase;

    public ShowCaseController(BrowseShowcaseUseCase browseShowcaseUseCase) {
        this.browseShowcaseUseCase = browseShowcaseUseCase;
    }

    @GetMapping
    List<EventOutput> browseShowCase(){
        return browseShowcaseUseCase.execute();
    }
}
