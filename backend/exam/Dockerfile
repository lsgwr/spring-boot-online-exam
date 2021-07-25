From python:3.8.5

ADD . /backend

WORKDIR /backend

RUN pip install -i https://pypi.tuna.tsinghua.edu.cn/simple pipenv && pipenv install

ENTRYPOINT ["pipenv", "run", "dev"]