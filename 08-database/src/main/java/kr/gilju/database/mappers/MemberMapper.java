package kr.gilju.database.mappers;


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

import kr.gilju.database.models.Members;

@Mapper
public interface MemberMapper {

    @Insert("INSERT INTO members (" +
            "user_id, user_pw, user_name, email, phone, " +
            "birthday, gender, postcode, addr1, addr2, " +
            "photo, is_out, is_admin, login_data, reg_data, edit_date) " +
            "VALUES (#{user_id}, MD5(#{user_pw}), #{user_name}, #{email}, #{phone}, " +
            "#{birthday}, #{gender}, #{postcode}, #{addr1}, #{addr2}, " +
            "#{photo}, #{is_out}, #{is_admin}, #{login_data}, #{reg_data}, #{edit_date})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Members input);

    @Update("UPDATE members SET " +
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
            "edit_date=NOW() " +
            "WHERE id = #{id}")
    int update(Members input);

    @Delete("DELETE FROM members WHERE id=#{id}")
    int delete(Members input); 

    @Select("SELECT " +
            "id, user_id, user_pw, user_name, email, phone, " +
            "birthday, gender, postcode, addr1, addr2, " +
            "photo, is_out, is_admin, login_data, reg_data, " +
            "edit_date FROM members " +
            "WHERE id = #{id}")
    @Results(id = "membersMap", value = {
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


    public Members selectItem(Members input);
    @Select("SELECT " +
            "id, user_id, user_pw, user_name, email, phone, " +
            "birthday, gender, postcode, addr1, addr2, " +
            "photo, is_out, is_admin, login_data, reg_data, " +
            "edit_date FROM members " +
            "ORDER BY id DESC")

    @ResultMap("membersMap")
    public List<Members> selectList(Members input);
}
