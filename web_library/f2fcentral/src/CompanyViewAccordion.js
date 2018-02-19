import React, {Component} from 'react';
/*import {
Accordion,
AccordionItem,
AccordionItemTitle,
AccordionItemBody
} from 'react-accessible-accordion';*/
import { PulseLoader } from 'react-spinners';

import { Accordion, AccordionItem } from 'react-sanfona';
import ChatView from './ChatView';

import './App.css';
import 'react-tabs/style/react-tabs.css';
import style from './App.css';

//import 'react-accessible-accordion/dist/react-accessible-accordion.css';
import CompanyViewFeedbackTitle from "./CompanyViewFeedbackTitle";
import FaFileImageO from 'react-icons/lib/fa/file-image-o';
import MdAnnouncement from 'react-icons/lib/md/announcement';
import CompanyFeedbackInputForm from "./CompanyFeedbackInputForm";
import DropzoneAvatar from "./DropzoneAvatar";
import FeedbackForumCommentView from "./FeedbackForumCommentView";

class CompanyViewAccordion extends Component {

  constructor(props) {
    super(props);
    this.state = {
      data : [],
      loading: true,
      showChat: false,
      showComment: false,
      chatIndex: null,
      chatTitle: '',
      filesToBeSent: [],
      filesPreview: [],
      printcount: 1,
      showDropzone: false,
      showModal: false
    },
    this.handleOpenModal = this.handleOpenModal.bind(this);
    this.handleCloseModal = this.handleCloseModal.bind(this);
    this.createFeedback = this.createFeedback.bind(this);
    this.fetchData = this.fetchData.bind(this);
    this.handleShowChat = this.handleShowChat.bind(this);
    this.handleBackButtonPressed = this.handleBackButtonPressed.bind(this);
    this.createFeedback = this.createFeedback.bind(this);
    this.openDropzone = this.openDropzone.bind(this);
    this.closeDropzone = this.closeDropzone.bind(this);
    this.handleShowCommentChange = this.handleShowCommentChange.bind(this);

  }

  handleShowCommentChange(e) {
    this.setState({showComment: e.showComment, commentIndex: e.index});
  }

  handleOpenModal(e){
    this.setState({ showModal: true});
  }

  handleCloseModal(){
    this.setState({ showModal: false});
  }

  openDropzone(e){
    this.setState({ showDropzone: true});
  }

   closeDropzone(){
    this.setState({ showDropzone: false});
   }

  fetchData(e){
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      }
    }).then(result=>result.json())
    .then(result=> {
      that.setState({data: result, loading: false})
    });
  }



  componentDidMount(){
    this.fetchData(null);
  }

  createFeedback(){
    {this.handleOpenModal()};
  /*return
  <ReactModal isOpen={this.state.showModal}>
    <button onClick={this.handleCloseModal}>Close Modal</button>
  </ReactModal>*/
}

handleShowChat(e) {
  this.setState({showChat: e.showChat, chatIndex: e.index, chatTitle: e.title});
}

handleBackButtonPressed() {
  this.setState({showChat: false, chatIndex: null, chatTitle: ''});
}

render() {
  let toRender = null;
  var that = this;
  if(!that.state.showChat && that.state.data.length > 0 && !that.state.showModal && !that.state.showDropzone && !that.state.showComment) {

    toRender = <Accordion>
      {that.state.data.map(function (item, index) {
        if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
        {
          return (
            <AccordionItem titleTag="span" title={<CompanyViewFeedbackTitle published={item.published} update={that.fetchData} onShowChat={that.handleShowChat} feedbackId={item.id} type={item.categoryFeedbacks[0].parameterId} title={item.textFeedbacks[0].text} date={item.createdAt} visibility={item.visibility} likes={item.likeCount} dislikes={item.dislikeCount} commentnumber={item.commentCount} blocked={item.blocked}/>}>
            </AccordionItem>
          )
        }
        return false;
      })}
    </Accordion>;
  }
  else if (this.state.loading) {
    var divStyle = {
      position: 'absolute',
      top: '50%',
      left: '50%',
      marginRight: '-50%',
      transform: 'translate(-50%, -50%)'
    };
    toRender = <div style={divStyle}><PulseLoader
      loading={this.state.loading}
      /></div>
  }
  else if(this.state.showChat) {
    toRender = <ChatView feedbackId={this.state.chatIndex} title={this.state.chatTitle} onBackButtonPressed={this.handleBackButtonPressed}/>
  }
  else if(this.state.showModal){
      toRender = <CompanyFeedbackInputForm onBackButtonSelected={this.handleCloseModal}/>;
  }
  else if(this.state.showDropzone){
    toRender = <DropzoneAvatar onDropzoneBackButtonSelected={this.closeDropzone}/>;
  }

  else if(this.state.showComment){
    toRender = <FeedbackForumCommentView post={this.state.data.find(function (a)
    {
       return a.id === instance.state.commentIndex;
    })}/>
  }
  else {
    toRender = <p>No Elements to show</p>;
    }

    return (
      <div className="CompanyViewAccordion">
        <FaFileImageO onClick={this.openDropzone} size={35}/>&nbsp;
          <MdAnnouncement onClick={this.handleOpenModal} size={35}/>{console.log(this.state.showModal)}
        <div className="CompanyFeedback">
            <ul>
              {toRender}
            </ul>
          </div>
        </div>
      );
    }
  }

  export default CompanyViewAccordion;
