package com.dk0124.respos.member.dao;

import com.dk0124.respos.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByNickName(String nickName);

}
