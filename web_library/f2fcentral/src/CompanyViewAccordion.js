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

import 'react-accessible-accordion/dist/react-accessible-accordion.css';
import CompanyViewFeedbackTitle from "./CompanyViewFeedbackTitle";
import FaFileImageO from 'react-icons/lib/fa/file-image-o';
import Dropzone from 'react-dropzone';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import MdDelete from 'react-icons/lib/md/delete';
import MdAnnouncement from 'react-icons/lib/md/announcement';
import ReactModal from 'react-modal/lib/components/Modal';
import CompanyFeedbackInputForm from "./CompanyFeedbackInputForm";

class CompanyViewAccordion extends Component {

  constructor(props) {
    super(props);
    this.state = {
      data : [],
      loading: true,
      showChat: false,
      chatIndex: null,
      chatTitle: '',
      filesToBeSent: [],
      filesPreview: [],
      printcount: 1,
      showModal: false
    },
    this.handleOpenModal = this.handleOpenModal.bind(this);
    this.handleCloseModal = this.handleCloseModal.bind(this);
    this.createFeedback = this.createFeedback.bind(this);
    this.fetchData = this.fetchData.bind(this);
    this.handleShowChat = this.handleShowChat.bind(this);
    this.handleBackButtonPressed = this.handleBackButtonPressed.bind(this);
  }


  handleOpenModal(){
    this.setState({ showModal: true});
  }


  handleCloseModal(){
    this.setState({ showModal: false});
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

  openFile(){
    return (
      <div>
        <Dropzone onDrop={(files) => this.onDrop(files)}/>
        <div>Upload a new image by dropping your file here or click to select file to upload.</div>
      </div>

    )
  }

  onDrop(acceptedFiles, rejectedFiles){
    var filesToBeSent=this.state.filesToBeSent;
    if(filesToBeSent.length == this.state.printcount) {
      filesToBeSent.push(acceptedFiles);
      var filesPreview = [];
      for(var i in filesToBeSent){
        filesPreview.push(<div>{filesToBeSent[i][0].name}
          <MuiThemeProvider>
            <a href="#"><MdDelete color="red" >clear</MdDelete>
          </a>
        </MuiThemeProvider>
      </div>
    )}
    this.setState({filesToBeSent, filesPreview});
  }
  else {
    alert("Please select a file to upload")
  }
}

createFeedback(){
  var that = this;
  return <ReactModal isOpen={that.state.showModal}>
    <button onClick={that.handleCloseModal}>Close Modal</button>
  </ReactModal>
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
  if(!that.state.showChat && that.state.data.length > 0) {

    toRender = <Accordion>
      {that.state.data.map(function (item, index) {
        if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
        {
          return (
            <AccordionItem titleTag="span" title={<CompanyViewFeedbackTitle published={item.published} update={that.fetchData} onShowChat={that.handleShowChat} feedbackId={item.id} type={item.categoryFeedbacks[0].parameterId} title={item.textFeedbacks[0].text} date={item.createdAt} visibility={item.visibility} likes={item.likeCount} dislikes={item.dislikeCount} commentnumber={item.commentCount}/>}>
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
  else {
    toRender = <p>No Elements to show</p>;
    }
    return (
      <div className="CompanyViewAccordion">
        <FaFileImageO onClick={this.openFile} size={35}/>&nbsp;
          <MdAnnouncement onClick={this.createFeedback} size={35}/>
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
