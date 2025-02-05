package kr.gilju.myshop.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gilju.myshop.models.Member;

@Mapper
public interface MemberMapper {

        /**
         * 
         * @param input
         * @return
         */
        @Insert("INSERT INTO member (" +
                        "user_id, user_pw, user_name, email, phone, " +
                        "birthday, gender, postcode, addr1, addr2, " +
                        "photo, is_out, is_admin, login_data, reg_data, edit_date" +
                        ") VALUES (" +
                        "#{user_id}, MD5(#{user_pw}), #{user_name}, #{email}, #{phone}, " +
                        "#{birthday}, #{gender}, #{postcode}, #{addr1}, #{addr2}, " +
                        "#{photo}, 'N', 'N', null, now(), now())")
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        int insert(Member input);

        /**
         * 수정하기
         * 
         * @param input
         * @return
         */
        @Update("<script>" +
                        "UPDATE member SET " +
                        // 아이디는 수정하지 않는다
                        // "user_id=#{user_id}, ",
                        "user_name=#{user_name}, " +
                        // 신규 비밀번호가 입력된 경우만 UPDATE 절에 추가함
                        "<if test='new_user_pw != null and new_user_pw != \"\"'>" +
                        "user_pw = MD5(#{new_user_pw})," +
                        "</if>" +
                        "email=#{email}, " +
                        "phone=#{phone}, " +
                        "birthday=#{birthday}, " +
                        "gender=#{gender, jdbcType=CHAR}, " +
                        "postcode=#{postcode}, " +
                        "addr1=#{addr1}, " +
                        "addr2=#{addr2}, " +
                        "photo = #{photo}, " +
                        "edit_date=now() " +
                        // 세션의 일련번호와 입력한 비밀번호가 일치할 경우만 수정
                        "WHERE id = #{id} AND user_pw = MD5(#{user_pw})" +
                        "</script>")
        public int update(Member input);

        /**
         * 삭제
         * 
         * @param input
         * @return
         */
        @Delete("DELETE FROM member WHERE id=#{id}")
        int delete(Member input);

        /**
         * 단일조회
         * @param input
         * @return
         */
        @Select("SELECT " +
                        "id, user_id, user_pw, user_name, email, phone, " +
                        "DATE_FORMAT(birthday,'%Y-%m-%d') as birthday, " +
                        "gender, postcode, addr1, addr2, photo, " +
                        "is_out, is_admin, login_data, reg_data, edit_date " +
                        "FROM member " +
                        "WHERE id = #{id}")
        @Results(id = "memberMap", value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user_id", column = "user_id"),
                        @Result(property = "user_pw", column = "user_pw"),
                        @Result(property = "user_name", column = "user_name"),
                        @Result(property = "email", column = "email"),
                        @Result(property = "phone", column = "phone"),
                        @Result(property = "birthday", column = "birthday"),
                        @Result(property = "gender", column = "gender"),
                        @Result(property = "postcode", column = "postcode"),
                        @Result(property = "addr1", column = "addr1"),
                        @Result(property = "addr2", column = "addr2"),
                        @Result(property = "photo", column = "photo"),
                        @Result(property = "is_out", column = "is_out"),
                        @Result(property = "is_admin", column = "is_admin"),
                        @Result(property = "login_data", column = "login_data"),
                        @Result(property = "reg_data", column = "reg_data"),
                        @Result(property = "edit_date", column = "edit_date")
        })
        public Member selectItem(Member input);

        /**
         * 다중조회
         * @param input
         * @return
         */
        @Select("SELECT " +
                        "user_id, user_pw, user_name, email, phone, " +
                        "DATE_FORMAT(birthday,'%Y-%m-%d') as birthday, " +
                        "gender, postcode, addr1, addr2, photo, " +
                        "is_out, is_admin, login_data, reg_data, edit_date " +
                        "FROM member ")
        @ResultMap("memberMap")
        public List<Member> selectList(Member input);

        /**
         * 아이디, 이메일 중복체크
         * 
         * @param input
         * @return
         */
        @Select("<script>" +
                        "SELECT COUNT(*) FROM member\n" +
                        "<where>" +
                        "<if test ='user_id != null'>user_id = #{user_id}</if>\n" +
                        "<if test ='email != null'>email = #{email}</if>\n" +
                        // 회원정보 수정시 내 정보는 제외하고 중복검사 수행
                        "<if test ='id != 0'>AND id != #{id}</if>\n" +
                        "</where>\n" +
                        "</script>")
        public int selectCount(Member input);

        /**
         * 아이디 찾기
         * 
         * @param input
         * @return
         */
        @Select("SELECT user_id FROM member " +
                        "WHERE user_name = #{user_name} AND email = #{email} AND is_out='N'")
        @ResultMap("memberMap") // resultMap 으로하면 에러남,,?!
        public Member findId(Member input);

        /**
         * 비밀번호 재 발급
         * 
         * @param input
         * @return
         */
        @Update("UPDATE member SET user_pw = MD5(#{user_pw}) " +
                        "WHERE user_id = #{user_id} AND email = #{email} AND is_out='N'")
        public int resetPw(Member input);

        /**
         * 로그인 처리
         * 
         * @param input
         * @return
         */
        @Select("SELECT \n" +
                        "id, user_id, user_pw, user_name, email, phone, birthday, gender, \n" +
                        "postcode, addr1, addr2, photo, is_out, is_admin, login_data, reg_data, edit_date \n" +
                        "FROM member \n" +
                        "WHERE user_id = #{user_id} AND user_pw = MD5(#{user_pw}) AND is_out='N'")
        @ResultMap("memberMap")
        public Member login(Member input);

        /**
         * 현재시간으로 업데이트 ( 이 부분은 궂이 안해도됨)
         * 
         * @param input
         * @return
         */
        @Update("UPDATE member SET login_data=NOW() WHERE id = #{id} AND is_out='N'")
        public int updateLoginDate(Member input);

        /**
         * 회원탈퇴
         * 탈퇴한 사람을 또 탈퇴처리할수 없기에 AND 조건문 붙이기
         * 
         * @param input
         * @return
         */
        @Update("UPDATE member \n" +
                        "SET is_out='Y', edit_date=NOW() \n" +
                        "WHERE id = #{id} AND user_pw = MD5(#{user_pw}) AND is_out = 'N'")
        public int out(Member input);

        /**
         * 탈퇴회원 조회 및 삭제 실패
         * 
         * @return
         */
        @Select("SELECT photo FROM member \n" +
                        "WHERE is_out='Y' AND \n" +
                        "edit_date < DATE_ADD(NOW(), interval -1 minute) AND \n" +
                        "photo IS NOT NULL")
        @ResultMap("memberMap")
        public List<Member> selectOutMembersPhoto();

        /**
         * 탈퇴회원 조회 및 삭제 실패
         * 
         * @return
         */
        @Delete("DELETE FROM member \n" +
                        "WHERE is_out='Y' AND \n" +
                        "edit_date < DATE_ADD(NOW(), interval -20 second)")
        public int deleteOutMembers();
}
