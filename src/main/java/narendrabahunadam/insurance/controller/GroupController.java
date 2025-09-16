package narendrabahunadam.insurance.controller;

import narendrabahunadam.insurance.entity.Group;
import narendrabahunadam.insurance.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
/*@PreAuthorize("hasRole('ADMIN')")*/
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        System.out.println("Ping endpoint hit");
        return ResponseEntity.ok("hi");
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        System.out.println("Upload endpoint hit");
        try {
            groupService.processExcel(file);
            return ResponseEntity.ok("Groups and plans uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

}