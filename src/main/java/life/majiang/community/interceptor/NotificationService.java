package life.majiang.community.interceptor;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by 叶志伟 on 2020/7/1.
 */
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    public Long unreadCount(Integer id) {
        NotificationExample  notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public PaginationDTO list(int userId, int page, int size){
        PaginationDTO paginationDTO=new PaginationDTO();
        int totalPage;
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        long totalCount = notificationMapper.countByExample(notificationExample);
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else
        {
            totalPage=totalCount/size+1;
        }
        if (page<1){
            page=1;
        }
        paginationDTO.setPagination(totalPage,page);
        int offset=size*(page-1);
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications=notificationMapper..selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOs=new ArrayList<>();
        for (Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification));
            notificationDTOs.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOs);

        return paginationDTO;

    }

    public PaginationDTO list(Long userId,int page,int size){
        PaginationDTO paginationDTO=new PaginationDTO();
        int totalPage;
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        int totalCOunt = (int)notificationMapper.countByExample(notificationExample);
        if (totalCOunt%size==0){
            totalPage=totalCOunt/size;
        }else{
            totalPage=totalCOunt/size+1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalCOunt,page);
        int offset=size*(page-1);
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOs=new ArrayList<>();
        for (Notification n:notifications){
            NotificationDTO n1=new NotificationDTO();
            BeanUtils.copyProperties(n,n1);
            n1.setTypeName(NotificationTypeEnum.nameOfType(n.getType()));
            notificationDTOs.add(n1);
        }
        paginationDTO.setData(notificationDTOs);
        return paginationDTO;
    }



    public PaginationDTO list(Integer id, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO=new PaginationDTO<>();
        int totalPage;
        NotificationExample  notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        int totalCount = (int)notificationMapper.countByExample(notificationExample);
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else {
            totalPage = totalCount / size + 1;
        }
        paginationDTO.setPagination(totalPage,page);
        //size*(page-1)
        Integer offset=size*(page-1);
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");

        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<NotificationDTO> notificationDTOList=new ArrayList<>();
        for (Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOList);
        return paginationDTO;

    }
    public Long unreadCount(Long userId){
        int userId1 = userId.intValue();
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId1).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }
    public NotificationDTO read(Long id, User u){
        Notification n
                =notificationMapper.selectByPrimaryKey(id);
        if (n==null){
           throw new  CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        n.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(n);
        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(n,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(n));
        return notificationDTO;
    }

    public NotificationDTO read(Long id, User u){
        Notification notification= notificationMapper.selectByPrimarKey(id);
        if (notification==null){
            throw  new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        int a=Integer.parseInt(NotificationTypeEnum.nameOfType((notification.getType())));
        notificationDTO.setType(a);
        return notificationDTO;
    }
}
