package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;


import life.majiang.community.dto.QuestionQueryDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;

import life.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by codedrinker on 2019/5/7.
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;


    public PaginationDTO list(int page, int size){
        if (page<1){
            page=1;
        }
        int offset=size*(page-1);
        List<Question> questionList=questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question  question:questionList){
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//快速把这个question对象的属性拷贝到对象questionDTO上面
            System.out.println("questionDTOListfirst"+questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
            System.out.println("questionDTOafter"+questionDTO);
            System.out.println("questionDTOafter"+questionDTOList);
        }
        paginationDTO.setQuestions(questionDTOList);
        Integer totalcount = questionMapper.count();
        paginationDTO.setPagination(totalcount,page,size);
        return paginationDTO;
    }
    public PaginationDTO list(Long userId,int page,int size){
        PaginationDTO paginationDTO=new PaginationDTO();
        int totalPage=0;
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        int totalCount = (int)questionMapper.countByExample(questionExample);
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }
         paginationDTO.setPagination(totalPage,page);
        int i = size * (page - 1);
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage
        }
        paginationDTO.setPagination(totalPage,page);
        int offset=size*(page-1);
        QuestionExample example=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions =questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question q:questions){
            User user = userMapper.selectByPrimaryKey(q.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalpage;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().addCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        if (totalCount%size==0){
            totalpage = totalCount / size;
        }else {
            totalpage = totalCount / size + 1;
        }
        paginationDTO.setPagination(totalpage,page);

        int offset=size*(page-1);
        QuestionExample example=new QuestionExample();
        example.createCriteria().addCreatorEqualTo(userId);
        List<Question> questionList=questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        for (Question  question:questionList){
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//快速把这个question对象的属性拷贝到对象questionDTO上面
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(int id) {
        Question question = questionMapper.getById(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        int creator = questionDTO.getCreator();
        User user = userMapper.findById(creator);
        questionDTO.setUser(user);
        return questionDTO;
    }

    //增加阅读数
    public void incView(int id) {
        Question question = questionMapper.getById(id);
        question.setViewCount(question.getViewCount()+1);
        questionMapper.updateByExampleSelective(question);
    }
    //增加阅读数
    public void incView1(int id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public PaginationDTO list1(int page, int size, String search, String sort, String tag) {
            if (StringUtils.isNotBlank(search)) {
                String[] tags = StringUtils.split(search, " ");
                search = Arrays
                        .stream(tags)
                        .filter(StringUtils::isNotBlank)
                        .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining("|"));
                System.out.println("search:"+search);
            }
        PaginationDTO paginationDTO=new PaginationDTO();
        int totalPage=1;
        QuestionQueryDTO questionQueryDTO=new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        if (StringUtils.isNotBlank(tag)) {
            System.out.println("tag"+tag);
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            System.out.println("tag1"+tag);
            questionQueryDTO.setTag(tag);
        }
        for (SortEnum sortEnum:SortEnum.values()){
            if (sortEnum.name().toLowerCase().equals(sort)){
                questionQueryDTO.setSort(sort);
                if (sortEnum==SortEnum.HOT7){
                    questionQueryDTO.setTime(System.currentTimeMillis());
                }
                break;
            }
        }
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }
        if (page<1){
            page=1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset=page<1?0:size*(page-1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question:questionList){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        paginationDTO.setData(questionDTOList);
        return null;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            questionMapper.insert(question);
        }else{
            //更新
            Question  dbQu=questionMapper.selectByPrimarykey(question.getId());
            if (dbQu==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            QuestionExample example=new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (i!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }


        }
    }
    public PaginationDTO list22(String search, String tag, String sort, Integer page, Integer size) {
     if (StringUtils.isNotBlank(search)){
         String[] tags= org.springframework.util.StringUtils.split(search," ");
         search = Arrays.stream(tags).filter(StringUtils::isNoneBlank).map(t -> t.replace("+", ""))
                 .filter(StringUtils::isNoneBlank)
                 .collect(Collectors.joining("|"));
     }
        PaginationDTO paginationDTO=new PaginationDTO();
        int totalPage;
        QuestionQueryDTO questionQueryDTO=new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        if (StringUtils.isNotBlank(tag)){
            tag=tag.replace("+","");
            questionQueryDTO.setTag(tag);
        }
        for (SortEnum sortEnum:SortEnum.values()){
            if (sortEnum.name().toLowerCase().queals(sort)){
                questionQueryDTO.setSearch(sort);
                if (sortEnum==SortEnum.HOT7){
                    questionQueryDTO.setTime(System.currentTimeMillis());
                }
            }
        }
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+!;
        }
        if (page<1){
            page=1;
        }
        paginationDTO.setPagination(totalPage,page);
        int offset=page<1?0:size*(page-1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions=questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }
        public List<QuestionDTO> selectRelated1(QuestionDTO questionDTO){
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags=StringUtils.split(questionDTO.getTag(),",");
        //正则
        String regexpTag=Arrays.stream(tags).filter(StringUtils::isNotBlank)
                .map(t->t.replace("+","").replace("*",))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions=questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOList=questions.stream().map(
                q->{QuestionDTO questionDTO1=new QuestionDTO();
                BeanUtils.copyProperties(q,questionDTO);
                return questionDTO;}
        ).collect(Collectors.toList());
        return questionDTOList;
    }
    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO){
        if (StringUtils.isBlank(questionDTO.getTag()){
            return new ArrayList<>();
        }
        String[] tags= org.springframework.util.StringUtils.split(questionDTO.getTag(),",");
        //正则
        String regexpTag=Arrays.stream(tags).filter(StringUtils::isNotBlank).
                map(t->t.replace("+","").replace("*","")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questionList = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOList=questionList.stream().map(q->{
                QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOList;

    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags= StringUtils.split(questionDTO.getTag(),",");
        //正则
        String regexpTag=Arrays.stream(tags).filter(StringUtils::isNoneBlank)
                .map(t->t.replace("+","").replace("*",""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions=questionExtMapper.selectRelated(question);
        List<QuestionDTO>  questionDtos=questions.stream().map(q->{
            QuestionDTO q=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return q;
        }).collect(Collectors.toList());
        return questionDtos;

    }
    public void createOrUpdate1(Question question){
        if (question.getId()==null){
            //创建
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else{
            Question dbQuestion=questionMapper.selectByPrimarykey(question.getId());
            if (dbQuestion==null){
                throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
            }
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example=new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);


        }
    }

    /*
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(String search, String tag, String sort, Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        if (StringUtils.isNotBlank(tag)) {
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }

        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase().equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }

        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);
        Integer offset = page < 1 ? 0 : size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //size*(page-1)
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            // 更新

            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            if (dbQuestion.getCreator().longValue() != question.getCreator().longValue()) {
                throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
            }

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
    */
}
