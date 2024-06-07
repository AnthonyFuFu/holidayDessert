//package com.holidaydessert.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.holidaydessert.model.Member;
//
//@RestController
//@RequestMapping("/api/member")
//@Api(tags = "Member Management")
//public class CRUDControllerExample {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    // GET 所有用戶或根據姓名和郵箱過濾用戶
//    @GetMapping
//    @ApiOperation(value = "Get all users or filter by name and email")
//    public ResponseEntity<List<Member>> getAllUsers(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String email) {
//        try {
//            List<Member> users;
//            if (name != null && email != null) {
//                users = memberRepository.findByNameAndEmail(name, email);
//            } else if (name != null) {
//                users = memberRepository.findByName(name);
//            } else if (email != null) {
//                users = memberRepository.findByEmail(email);
//            } else {
//                users = memberRepository.findAll();
//            }
//            return ResponseEntity.ok(users);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    // POST 創建新用戶
//    @PostMapping
//    @ApiOperation(value = "Create a new user with name and email")
//    public ResponseEntity<Member> createUser(@RequestBody Member user) {
//        try {
//        	Member savedUser = memberRepository.save(user);
//            return ResponseEntity.status(201).body(savedUser);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    // GET 根據ID獲取用戶
//    @GetMapping("/{id}")
//    @ApiOperation(value = "Get user by ID")
//    public ResponseEntity<Member> getUserById(@PathVariable Long id) {
//        return memberRepository.findById(id)
//                .map(user -> ResponseEntity.ok().body(user))
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // PUT 更新用戶
//    @PutMapping("/{id}")
//    @ApiOperation(value = "Update user by ID")
//    public ResponseEntity<Member> updateUser(@PathVariable Long id, @RequestBody Member userDetails) {
//        return memberRepository.findById(id)
//                .map(user -> {
//                    user.setMemName(userDetails.getMemName());
//                    user.setMemEmail(userDetails.getMemEmail());
//                    Member updatedUser = memberRepository.save(user);
//                    return ResponseEntity.ok().body(updatedUser);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // DELETE 刪除用戶
//    @DeleteMapping("/{id}")
//    @ApiOperation(value = "Delete user by ID")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        return memberRepository.findById(id)
//                .map(user -> {
//                	memberRepository.delete(user);
//                    return ResponseEntity.ok().build();
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//}
