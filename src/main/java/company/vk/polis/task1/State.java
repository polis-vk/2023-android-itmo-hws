package company.vk.polis.task1;

public enum State {
    READ,
    UNREAD,
    DELETED;
    private Integer id_of_deleter;
    public Integer getId_of_deleter() { return id_of_deleter; }
    public void setId_of_deleter(Integer id_of_deleter) { this.id_of_deleter = id_of_deleter; }
}
