package org.shortln.controllers;

import lombok.Data;
import org.shortln.models.Account;
import org.shortln.models.Link;
import org.shortln.models.LinksGroup;
import org.shortln.repositories.LinkLogRepository;
import org.shortln.repositories.LinkRepository;
import org.shortln.repositories.LinksGroupRepository;
import org.shortln.tools.SessionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

@Data
class LinkIn {
    private Long gid;
    private String title;
    private String jumpUrl;
    private Link.Expire expire;
}
@Data
class LinksGroupIn {
    private String name;
}

@RestController
@RequestMapping("/links")
public class LinkController {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private LinksGroupRepository linksGroupRepo;
    @Autowired
    private LinkLogRepository linkLogRepo;

    @PostMapping("")
    public Link createLink(@RequestBody LinkIn linkIn) {
        var ln = Link.builder()
                .title(linkIn.getTitle())
                .jumpUrl(linkIn.getJumpUrl())
                .expire(linkIn.getExpire())
                .group(LinksGroup.builder().id(linkIn.getGid()).build())
                .build();
        return linkRepository.save(ln);
    }

    @PostMapping("groups")
    public LinksGroup createLinksGroup(@RequestBody LinksGroupIn linksGroupIn) {
        var lg = LinksGroup.builder()
                .name(linksGroupIn.getName())
                .author(Account.builder().id(
                        SessionTool.curAccount().getId()
                ).build())
                .build();
        return linksGroupRepo.save(lg);
    }

    @GetMapping("groups")
    public Pagination<LinksGroup> getLinksGroup(
            @RequestBody Pagination.Query<LinksGroup> query
    ) {
        var newLG = LinksGroup.builder()
                .name(query.getStr())
                .build();
        var matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        return query.getPagination(linksGroupRepo, Example.of(newLG, matcher));
    }
}
