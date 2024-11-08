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
         * 
         * @param input
         * @return
         */
        @Update("UPDATE member SET " +
                        "user_id=#{user_id}, " +
                        "user_pw=MD5(#{user_pw}), " +
                        "user_name=#{user_name}, " +
                        "email=#{email}, " +
                        "phone=#{phone}, " +
                        "birthday=#{birthday}, " +
                        "gender=#{gender}, " +
                        "postcode=#{postcode}, " +
                        "addr1=#{addr1}, " +
                        "addr2=#{addr2}, " +
                        "edit_date=now() " +
                        "WHERE id = #{id}")
        int update(Member input);

        @Delete("DELETE FROM member WHERE id=#{id}")
        int delete(Member input);


        /**
         * 
         * @param input
         * @return
         */
        @Select("SELECT " +
                        "user_id, user_pw, user_name, email, phone, " +
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
         * 
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
         * 
         * @param input
         * @return
         */
        @Select("<script>" +
                        "SELECT COUNT(*) FROM member\n" +
                        "<where>" +
                        "<if test ='user_id != null'>user_id = #{user_id}</if>\n" +
                        "<if test ='email != null'>email = #{email}</if>\n" +
                        "</where>\n" +
                        "</script>")
        public int selectCount(Member input);


        /**
         * 
         * @param input
         * @return
         */
        @Select("SELECT user_id FROM member " +
                        "WHERE user_name = #{user_name} AND email = #{email}")
        @ResultMap("memberMap") // resultMap 으로하면 에러남,,?!
        public Member findId(Member input);
}
