package com.oms.dto;

public class IssueReplyDTO {
    private Integer replyId;
    private Integer issueId;
    private Integer repliedBy;
    private String repliedByName;
    private String replyMessage;

    public Integer getReplyId() { return replyId; }
    public void setReplyId(Integer replyId) { this.replyId = replyId; }

    public Integer getIssueId() { return issueId; }
    public void setIssueId(Integer issueId) { this.issueId = issueId; }

    public Integer getRepliedBy() { return repliedBy; }
    public void setRepliedBy(Integer repliedBy) { this.repliedBy = repliedBy; }

    public String getRepliedByName() { return repliedByName; }
    public void setRepliedByName(String repliedByName) { this.repliedByName = repliedByName; }

    public String getReplyMessage() { return replyMessage; }
    public void setReplyMessage(String replyMessage) { this.replyMessage = replyMessage; }
}