package lsgwr.exam.qo;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/***********************************************************
 * @note      : 要下载的文件的路径
 * @author    :  梁山广
 * @version   : V1.0 at 2019/5/19 20:10
 ***********************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadQo {
    String path;
}
