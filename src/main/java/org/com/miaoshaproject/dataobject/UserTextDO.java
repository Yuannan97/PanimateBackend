package org.com.miaoshaproject.dataobject;

public class UserTextDO extends UserTextDOKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_text.text
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    private String text;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_text.name
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_text.type
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_text.time
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    private String time;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_text.text
     *
     * @return the value of user_text.text
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public String getText() {
        return text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_text.text
     *
     * @param text the value for user_text.text
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_text.name
     *
     * @return the value of user_text.name
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_text.name
     *
     * @param name the value for user_text.name
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_text.type
     *
     * @return the value of user_text.type
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_text.type
     *
     * @param type the value for user_text.type
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_text.time
     *
     * @return the value of user_text.time
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public String getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_text.time
     *
     * @param time the value for user_text.time
     *
     * @mbg.generated Mon Jun 22 20:22:52 EDT 2020
     */
    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }
}