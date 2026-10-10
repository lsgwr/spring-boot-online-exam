package lsgwr.exam.security;

public record CurrentUser(String id, String username, int roleId) {
    public boolean admin() {
        return roleId == 1;
    }

    public boolean staff() {
        return roleId == 1 || roleId == 2;
    }
}
