/**
 * Created by flo on 14.07.16.
 */
import {Component} from 'angular2/core'
import {CourseService} from '../services/course.service'
import {AutoGrowDirective} from "../directives/auto-grow.directive";

@Component({
    selector: 'courses',
    template: `
        <h2>Courses</h2>
        {{ title }}
        <input type="text" autoGrow />
        <ul>
            <li *ngFor="#course of courses">
                {{course}}
            </li>
        </ul>
        `,
    providers: [CourseService],
    directives: [AutoGrowDirective]
})
export class CoursesComponent {
    title: string = "The title of courses page";
    courses;

    constructor(courseService: CourseService){
        this.courses = courseService.getCourses();
    }
}