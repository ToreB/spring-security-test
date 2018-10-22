package no.toreb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class SecuredController {

    @GetMapping("/secured1")
    @PreAuthorize("hasRole('ROLE_ROLE1')")
    public String secured1() {
        return "secured1";
    }

    @GetMapping("/secured2/{text}")
    @PreAuthorize("@securityService.allowAccess(#text)")
    public String secured2(@PathVariable final String text) {
        return "secured2: " + text;
    }

    @PostMapping
    @PreAuthorize("@securityService.allowAccess(#event)")
    public ResponseEntity<String> createEvent(@RequestBody final Event event) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Event was created!");
    }
}
