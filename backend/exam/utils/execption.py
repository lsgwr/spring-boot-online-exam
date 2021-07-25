class AuthenticateException(Exception):
    def __init__(self, detail=''):
        super().__init__()
        self.code = 401
        self.msg = '身份认证失败'
        self.detail = detail
        