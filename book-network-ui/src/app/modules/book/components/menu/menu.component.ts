import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {routes} from '../../../../app.routes';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit{
  ngOnInit(): void {
    this.activeMenu();
  }

  logout() {
    localStorage.clear();
    window.location.reload();
  }

  private activeMenu() {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', ()=>{
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      });
    });
  }
}
