import time
from pathlib import Path
from loguru import logger


root_path = Path(__file__).parent.parent

# 定位到log日志文件
log_path = root_path.joinpath('logs')
log_path.mkdir(parents=True, exist_ok=True)

log_path_info = log_path.joinpath(f'{time.strftime("%Y-%m-%d")}_info.log')
log_path_warning = log_path.joinpath(f'{time.strftime("%Y-%m-%d")}_warning.log')
log_path_error = log_path.joinpath(f'{time.strftime("%Y-%m-%d")}_error.log')

# 日志简单配置 文件区分不同级别的日志
logger.add(log_path_info,
           rotation="10 MB",
           encoding='utf-8',
           enqueue=True,
           level='INFO',
           retention=5,
           compression='zip')

logger.add(log_path_warning,
           rotation="10 MB",
           encoding='utf-8',
           enqueue=True,
           level='WARNING',
           retention=5,
           compression='zip')

logger.add(log_path_error,
           rotation="10 MB",
           encoding='utf-8',
           enqueue=True,
           level='ERROR',
           retention=5,
           compression='zip')

__all__ = ["logger"]